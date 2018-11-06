//
// Created by wangxinshuo on 11/1/18.
//

#ifndef KV_UTILS_H
#define KV_UTILS_H

#include <string>
#include <cstring>
#include <string.h>
#include <sys/stat.h>
#include <iostream>
#include <fstream>

#include "TreeMap.h"
#include "Utils.h"

#define POINTER_LENGTH_IN_INDEX 8
#define VALUE_LENGTH_COUNT 4

class Resources {
public:
    Resources(const char *name,
              const char *indexFileName = "/index.db",
              const char *valueFileName = "/value.db");

    void writeIndex(const char *data, size_t len);

    void readIndex(char *data, size_t start, size_t len);

    long long getIndexFileLength();

    void writeValue(const char *data, size_t len);

    void readValue(char *data, size_t start, size_t len);

    long long getValueFileLength();

    void close();

    ~Resources();

private:
    FILE *indexFile;
    FILE *valueFile;
};

class Convert {
public:
    static void longToCharArray(long long position, char *data);

    static long long charArrayToLong(const char *data);

    static void intToCharArray(int position, char *data);

    static int charArrayToInt(const char *data);

};


class Rebuild {
public:
    static void rebuild(Resources *resources, TreeMap *treeMap);
};


#endif //KV_UTILS_H
