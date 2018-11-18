//
// Created by wangxinshuo on 11/17/18.
//
#include "index.h"


Index::Index(const char *path) {
    // std::string dir(path);
    // dir.append("/index.db");
    // create::CreateFile(dir.data());
    // this->tree = new bpt::bplus_tree(dir.data());
}

void Index::add(const char *key, long long value) {
    // bpt::key_t insert_key(key);
    // bpt::value_t insert_value = value;
    // this->index_lock.lock();
    // int result = this->tree->insert(insert_key, insert_value);
    // this->index_lock.unlock();
    // if (result == 1) {
    //     this->index_lock.lock();
    //     this->tree->update(insert_key, insert_value);
    //     this->index_lock.unlock();
    // }
}

long long Index::get(const char *key) {
    // bpt::key_t select_key(key);
    // bpt::value_t select_value;
    // this->index_lock.lock();
    // int result = this->tree->search(select_key, &select_value);
    // this->index_lock.unlock();
    // if (result == 0)
    //     return select_value;
    // return -1;
}

Index::~Index() {
    // delete this->tree;
}
