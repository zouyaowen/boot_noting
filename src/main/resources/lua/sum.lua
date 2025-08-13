-- lua脚本开发测试在java目录在，生产打包一般在resources下，使用

local a = tonumber(ARGV[1]) * 2
local b = tonumber(ARGV[2]) * 2
return a + b