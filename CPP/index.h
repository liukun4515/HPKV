//
// Created by wangxinshuo on 11/17/18.
//

#ifndef KV_INDEX_H
#define KV_INDEX_H

#include <string>
#include <mutex>

#include "bt.h"
#include "conf.h"
#include "create.h"

class Index {
public:
    explicit Index(const char *);

    void add(const char *, long long);

    long long get(const char *);

    ~Index();

private:
    bpt::bplus_tree *tree;
    std::mutex index_lock;
};

#endif //KV_INDEX_H
