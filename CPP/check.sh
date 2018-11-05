make
cp test/test.cc ./
g++ -o run test.cc -L./lib -lengine
rm -f *.o
