# Output script for 4.3.5
set terminal postscript color solid
set output "output-435.eps"
set xlabel "Threads"
set ylabel "Time"
plot "benchmarks-20150921-435.txt" using 2:3 title "Local Long" with errorlines,"benchmarks-20150921-434.txt" using 2:3 title "AtomicLong" with errorlines