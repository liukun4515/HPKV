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

    TreeMap *EngineRace::treeMap;
    Resources *EngineRace::resources;

// 1. Open engine
    RetCode EngineRace::Open(const std::string &name, Engine **eptr) {
        *eptr = nullptr;
        auto *engine_race = new EngineRace(name);
        // add by myself
        resources = new Resources(name.data());
        treeMap = new TreeMap;
        Rebuild::rebuild(resources, treeMap);
        //
        *eptr = engine_race;
        return kSucc;
    }

// 2. Close engine
    EngineRace::~EngineRace() {
        treeMap->close();
        resources->close();
        delete resources;
        delete treeMap;
    }

// 3. Write a key-value pair into engine
    RetCode EngineRace::Write(const PolarString &key, const PolarString &value) {
        int intValueSize = static_cast<int>(value.size());

        char *charValueSize = new char[VALUE_LENGTH_COUNT];
        Convert::intToCharArray(intValueSize, charValueSize);
        mtx.lock();
        long long value_len = resources->getValueFileLength();
        resources->writeValue(charValueSize, VALUE_LENGTH_COUNT);
        delete[](charValueSize);

        resources->writeValue(value.data(), value.size());

        char *key_size = new char;
        *key_size = static_cast<char>(key.size());
        resources->writeIndex(key_size, 1);
        delete key_size;

        char *index_position = new char[POINTER_LENGTH_IN_INDEX];
        Convert::longToCharArray(value_len, index_position);
        resources->writeIndex(index_position, POINTER_LENGTH_IN_INDEX);
        delete[] index_position;

        auto *polarString = new polar_race::PolarString(key.data());
        treeMap->add(polarString, value_len);
        mtx.unlock();
        return kSucc;
    }

// 4. Read value of a key
    RetCode EngineRace::Read(const PolarString &key, std::string *value) {
        PolarString select(key.data());
        long long position = treeMap->get(&select);
        mtx.lock();
        char *charValueLength = new char[VALUE_LENGTH_COUNT];
        resources->readValue(charValueLength,
                             static_cast<size_t>(position),
                             VALUE_LENGTH_COUNT);
        int intValueLength = Convert::charArrayToInt(charValueLength);
        char *data = new char[intValueLength];
        resources->readValue(data, static_cast<size_t>(position + VALUE_LENGTH_COUNT),
                             static_cast<size_t>(intValueLength));
        mtx.unlock();
        value->clear();
        value->append(data, static_cast<unsigned long>(intValueLength));
        delete[](data);
        delete[](charValueLength);
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
