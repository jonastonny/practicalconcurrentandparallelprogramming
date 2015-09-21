# Output script for 4.3.4
set terminal postscript color solid
set output "output-434-combined.eps"
set xlabel "Threads"
set ylabel "Time"
plot "benchmarks-20150919-431.txt" using 2:3 title "LongCounter" with errorlines,"benchmarks-20150919-434.txt" using 2:3 title "AtomicLong" with errorlines
