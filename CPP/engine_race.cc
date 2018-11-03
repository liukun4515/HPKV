// Copyright [2018] Alibaba Cloud All rights reserved
#include "engine_race.h"

namespace polar_race {

    std::mutex mtx;

    RetCode Engine::Open(const std::string &name, Engine **eptr) {
        return EngineRace::Open(name, eptr);
    }

    Engine::~Engine() = default;

/*
 * Complete the functions below to implement you own engine
 */

    TreeMap *EngineRace::treeMap= new TreeMap;
    std::string EngineRace::file_name;

// 1. Open engine
    RetCode EngineRace::Open(const std::string &name, Engine **eptr) {
        *eptr = nullptr;
        auto *engine_race = new EngineRace(name);
        // add by myself
        createDirectory(name);
        restore(name + "/index.db",treeMap);
        file_name = name;
        *eptr = engine_race;
        return kSucc;
    }

// 2. Close engine
    EngineRace::~EngineRace() {
        treeMap->close();
        delete treeMap;
    }

// 3. Write a key-value pair into engine
    RetCode EngineRace::Write(const PolarString &key, const PolarString &value) {
        std::string index_name = file_name + "/index.db";
        std::string store_name = file_name + "/store.db";
        mtx.lock();
        //first write into store
        std::fstream::pos_type store_len = fileLength(store_name.data());
        write(store_name.data(),value.data());
        // second write into index
        write(index_name.data(),key.data());
        char * position = new char[8];
        positionToCharArray(store_len,position);
        write(index_name.data(),position);
        delete[] position;
        // add into map
        polar_race::PolarString polarString(key.data());
        treeMap->add(&polarString,store_len);
        mtx.unlock();
        return kSucc;
    }

// 4. Read value of a key
    RetCode EngineRace::Read(const PolarString &key, std::string *value) {
        long long position =
                treeMap->get(new polar_race::PolarString(key.data()));
        std::string store_name = file_name + "/store.db";
        char *data = new char[4096];
        mtx.lock();
        read(store_name.data(), static_cast<size_t>(position), data);
        mtx.unlock();
        value->append(data);
        delete[](data);
        return kSucc;
    }

/*
 * NOTICE: Implement 'Range' data quarter-final,
 *         you can skip it data preliminary.
 */
// 5. Applies the given Vistor::Visit function to the result
// of every key-value pair data the key range [first, last),
// data order
// lower=="" is treated as a key before all keys data the database.
// upper=="" is treated as a key after all keys data the database.
// Therefore the following call will traverse the entire database:
//   Range("", "", visitor)
    RetCode EngineRace::Range(const PolarString &lower, const PolarString &upper,
                              Visitor &visitor) {
        return kSucc;
    }

}  // namespace polar_race
