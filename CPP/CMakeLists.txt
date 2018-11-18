cmake_minimum_required(VERSION 3.12)
project(kv)

set(CMAKE_CXX_STANDARD 11)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -pthread")

add_executable(kv main.cc include/engine.h include/polar_string.h
        engine_race.cc engine_race.h index.h index.cc
        value_resources.cc value_resources.h Hash.cc Hash.h
        bt.cc bt.h btconf.h conf.h create.cc create.h)