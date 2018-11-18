//
// Created by wangxinshuo on 11/17/18.
//

#ifndef KV_CREATE_H
#define KV_CREATE_H

#include <cerrno>
#include <fcntl.h>
#include <cstring>
#include <iostream>
#include <sys/stat.h>

class create {
public:
    static void CreateDirectory(const char *);

    static void CreateFile(const char *);
};


#endif //KV_CREATE_H
