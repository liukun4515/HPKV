//
// Created by wangxinshuo on 11/1/18.
//


#include "TreeMap.h"

void TreeMap::add(polar_race::PolarString *key, long long value) {
    this->iter = data.find(*key);
    if (iter == data.end()) {
        this->data.insert(std::pair<polar_race::PolarString,
                long long>(*key, value));
    } else {
        iter->second = value;
    }
}

long long TreeMap::get(polar_race::PolarString *key) {
    this->iter = this->data.find(*key);
    if (this->iter != this->data.end()) {
        return this->iter->second;
    }
    return -1;
}


void TreeMap::close() {
    std::cout<<"Close"<<std::endl;
    data.clear();
}