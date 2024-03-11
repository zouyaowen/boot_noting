package com.trip.noting.graph;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DirectedAcyclicGraph {

    private final ConcurrentHashMap<String, Task> tasKMap = new ConcurrentHashMap<>();
    private final HashMap<String, Boolean> tasKCore = new HashMap<>();
    private final HashMap<String, HashMap<String, Boolean>> tasKDependency = new HashMap<>();
    private final HashMap<String, HashMap<String, Boolean>> reverseDependency = new HashMap<>();
    private Boolean checkDepPass = false;
    public Executor executor;
    private final AtomicInteger anonymousTaskIndex = new AtomicInteger(0);

    public interface TaskContext {

    }

    public interface Task {

        String getName();

        boolean skip(TaskContext context);

        void process(TaskContext context);
    }

    @FunctionalInterface
    public interface ProcessFunc {
        void process(TaskContext context);
    }

    @FunctionalInterface
    public interface SkipFunc {
        boolean skip(TaskContext context);
    }

    public DirectedAcyclicGraph addTask(ProcessFunc processFunc) {
        int taskIndex = anonymousTaskIndex.addAndGet(1);
        Task task = new Task() {

            @Override
            public String getName() {
                return "task_" + taskIndex;
            }

            @Override
            public boolean skip(TaskContext context) {
                return false;
            }

            @Override
            public void process(TaskContext context) {
                processFunc.process(context);
            }
        };
        addTask(task, false);
        return this;
    }

    public DirectedAcyclicGraph addTask(ProcessFunc processFunc, String... dependencyTask) {
        int taskIndex = anonymousTaskIndex.addAndGet(1);
        Task task = new Task() {

            @Override
            public String getName() {
                return "task_" + taskIndex;
            }

            @Override
            public boolean skip(TaskContext context) {
                return false;
            }

            @Override
            public void process(TaskContext context) {
                processFunc.process(context);
            }
        };
        addTask(task, false);
        return this;
    }


    public DirectedAcyclicGraph addTask(String name, ProcessFunc processFunc, String... dependencyTask) {
        Task task = new Task() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public boolean skip(TaskContext context) {
                return false;
            }

            @Override
            public void process(TaskContext context) {
                processFunc.process(context);
            }
        };
        addTask(task, false, dependencyTask);
        return this;
    }

    public DirectedAcyclicGraph addTask(String name, ProcessFunc processFunc, boolean core, String... dependencyTask) {
        Task task = new Task() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public boolean skip(TaskContext context) {
                return false;
            }

            @Override
            public void process(TaskContext context) {
                processFunc.process(context);
            }
        };
        addTask(task, core, dependencyTask);
        return this;
    }

    public DirectedAcyclicGraph addTask(String name, ProcessFunc processFunc, SkipFunc skipFunc, boolean core, String... dependencyTask) {
        Task task = new Task() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public boolean skip(TaskContext context) {
                return skipFunc.skip(context);
            }

            @Override
            public void process(TaskContext context) {
                processFunc.process(context);
            }
        };
        addTask(task, core, dependencyTask);
        return this;
    }

    public DirectedAcyclicGraph addTask(ProcessFunc processFunc, SkipFunc skipFunc, boolean core, String... dependencyTask) {
        int taskIndex = anonymousTaskIndex.addAndGet(1);
        Task task = new Task() {
            @Override
            public String getName() {
                return "task-" + taskIndex;
            }

            @Override
            public boolean skip(TaskContext context) {
                return skipFunc.skip(context);
            }

            @Override
            public void process(TaskContext context) {
                processFunc.process(context);
            }
        };
        addTask(task, core, dependencyTask);
        return this;
    }

    public DirectedAcyclicGraph addTask(ProcessFunc processFunc, boolean core) {
        int taskIndex = anonymousTaskIndex.addAndGet(1);
        Task task = new Task() {
            @Override
            public String getName() {
                return "task-" + taskIndex;
            }

            @Override
            public boolean skip(TaskContext context) {
                return false;
            }

            @Override
            public void process(TaskContext context) {
                processFunc.process(context);
            }
        };
        addTask(task, core);
        return this;
    }

    public DirectedAcyclicGraph addTask(Task task, boolean core, String... dependencyTask) {

        if (checkDepPass) {
            throw new RuntimeException("the task dependency check finish");
        }

        if (task.getName() == null || task.getName().trim().isEmpty()) {
            throw new RuntimeException("the task name is empty");
        }

        if (tasKCore.containsKey(task.getName())) {
            throw new RuntimeException("the task already exists");
        }

        registerTask(task);

        for (String preTask : dependencyTask) {
            registerTask(task);
            addDependency(task.getName(), preTask);
        }
        tasKCore.put(task.getName(), core);
        return this;
    }

    private void addDependency(String task, String preTask) {
        // task依赖preTask
        tasKDependency.compute(task, (k, v) -> {
            if (v == null) {
                v = new HashMap<>();
            }
            v.put(preTask, true);
            return v;
        });

        // 链路反向依赖关系
        reverseDependency.compute(preTask, (k, v) -> {
            if (v == null) {
                v = new HashMap<>();
            }
            v.put(task, true);
            return v;
        });
    }

    private void registerTask(Task task) {
        tasKMap.put(task.getName(), task);
    }

    public DirectedAcyclicGraph build(TaskContext... taskContexts) {
        if (tasKCore.isEmpty()) {
            return this;
        }
        if (executor == null) {
            executor = new ThreadPoolExecutor(2, 5, 5L, TimeUnit.MINUTES, new ArrayBlockingQueue<>(500), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
        }
        // 检查依赖关系
        checkDependency();
        // 检测是否有环
        this.tasKDependency.forEach((k, v) -> {
            HashMap<String, Boolean> visited = new HashMap<>();
            dfs(k, visited);
        });
        return this;
    }

    private void dfs(String name, HashMap<String, Boolean> visited) {
        if (visited.containsKey(name)) {
            throw new RuntimeException(String.format("task %s cycle detected", name));
        }
        visited.put(name, true);
        tasKDependency.computeIfPresent(name, (k, v) -> {
            v.forEach((preTaskName, preTaskV) -> {
                dfs(preTaskName, visited);
            });
            return v;
        });
    }

    /**
     * 检查依赖关系，规避依赖不存在的任务
     */
    private void checkDependency() {
        if (this.tasKDependency.isEmpty()) {
            return;
        }
        this.tasKDependency.forEach((child, parents) -> {
            parents.forEach((parent, v) -> {
                if (!tasKCore.containsKey(parent)) {
                    throw new RuntimeException(String.format("the dependency task:%s not exists", parent));
                }
            });
        });
        this.checkDepPass = true;
    }

    public void execute(TaskContext taskContext) {
        Graph graph = newGraphInstance();
        try {
            graph.execute(taskContext);
        } catch (InterruptedException e) {
            throw new RuntimeException(String.format("task exec interrupted error:%s", e.getMessage()));
        }
        if (graph.earlyReturn) {
            // throw new RuntimeException(String.format("task exec error:%s", graph.errorMessage));
            System.out.printf("graph exec error:%s%n", graph.errorMessage);
        }
    }

    private Graph newGraphInstance() {
        Graph graph = new Graph();
        graph.nodeMap = new HashMap<>();
        graph.dag = this;
        graph.earlyReturn = false;
        graph.signal = new CountDownLatch(1);
        tasKMap.forEach((k, v) -> graph.nodeMap.put(k, convertTaskToNode(v)));
        graph.finishedCount = new AtomicInteger(graph.nodeMap.size());
        return graph;
    }

    private GraphNode convertTaskToNode(Task task) {
        GraphNode graphNode = new GraphNode();
        graphNode.nodeName = task.getName();
        graphNode.core = tasKCore.get(task.getName());
        graphNode.countDownLatchList = new ArrayList<>();
        return graphNode;
    }

    private class Graph {
        private HashMap<String, GraphNode> nodeMap;
        private DirectedAcyclicGraph dag;
        private Boolean earlyReturn;
        private String errorMessage;
        private AtomicInteger finishedCount;
        private CountDownLatch signal;

        public void execute(TaskContext taskContext) throws InterruptedException {
            this.preExecute();
            nodeMap.forEach((k, node) -> executor.execute(() -> {
                try {
                    node.execute(taskContext);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    // 核心逻辑异常提前返回
                    if (node.core) {
                        this.errorMessage = e.getMessage();
                        this.earlyReturn = true;
                    }
                } finally {
                    // 提前返回
                    if (this.earlyReturn) {
                        this.signal.countDown();
                        // 所有节点通知提前返回
                        nodeMap.forEach((nodeName, stopNode) -> {
                            stopNode.earlyReturn = true;
                        });
                        // 通知下游开始执行
                        for (CountDownLatch countDownLatch : node.countDownLatchList) {
                            countDownLatch.countDown();
                        }
                    }
                    // 主任务结束
                    if (finishedCount.get() > 0 && finishedCount.addAndGet(-1) == 0) {
                        this.signal.countDown();
                    }
                }
            }));
            // 主线程阻塞等待
            if (this.signal.await(60, TimeUnit.MINUTES)) {
                // 核查是否提前返回
                if (this.earlyReturn) {
                    System.out.printf("task exec early return error:%s%n", errorMessage);
                    // throw new RuntimeException(String.format("graph exec error:%s", errorMessage));
                }
            }
        }

        private void preExecute() {
            nodeMap.forEach((taskName, node) -> {
                // 纯并发关系没有依赖构建信息
                HashMap<String, Boolean> dependencyNodeList = this.dag.tasKDependency.get(taskName);
                if (dependencyNodeList == null) {
                    node.prevTaskCount = new AtomicInteger(0);
                } else {
                    node.prevTaskCount = new AtomicInteger(dependencyNodeList.size());
                }
                if (node.prevTaskCount.get() > 0) {
                    assert dependencyNodeList != null;
                    node.countDownLatch = new CountDownLatch(dependencyNodeList.size());
                    dependencyNodeList.forEach((k, v) -> {
                        GraphNode prevNode = nodeMap.get(k);
                        prevNode.countDownLatchList.add(node.countDownLatch);
                    });
                }
            });
        }
    }

    private class GraphNode {
        // 基础信息
        private String nodeName;
        private boolean core;
        private boolean earlyReturn;

        // 并发控制-依赖任务数量
        private AtomicInteger prevTaskCount;
        // 并发控制-当前节点信号接收器
        private CountDownLatch countDownLatch;
        // 并发控制-需要通知的下游节点信号
        private List<CountDownLatch> countDownLatchList;

        public void execute(TaskContext taskContext) throws InterruptedException {
            LocalDateTime start = LocalDateTime.now();
            System.out.printf("---------%s exec start ----%n", nodeName);
            if (prevTaskCount.get() == 0 || countDownLatch.await(2, TimeUnit.MINUTES)) {
                // 耗时去除依赖任务的耗时
                start = LocalDateTime.now();
                if (!this.earlyReturn) {
                    executeTask(taskContext);
                } else {
                    System.out.printf("---------%s exec early return ----%n", nodeName);
                }
            }
            System.out.printf("---------%s exec finish ----%n", nodeName);
            // 通知下游节点
            for (CountDownLatch downLatch : countDownLatchList) {
                downLatch.countDown();
            }
            if (prevTaskCount.get() > 0) {
                prevTaskCount.addAndGet(-1);
            }
            LocalDateTime end = LocalDateTime.now();
            Duration between = Duration.between(start, end);
            long millis = between.toMillis();
            System.out.println(this.nodeName + "耗时：" + millis + "毫秒");
        }

        private void executeTask(TaskContext taskContext) {
            Task task = tasKMap.get(nodeName);
            // 判断是否跳过
            if (!task.skip(taskContext)) {
                task.process(taskContext);
            }
        }
    }
}
