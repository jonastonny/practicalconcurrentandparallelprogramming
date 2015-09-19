# Output script for 4.3.1
set terminal postscript color solid
set output "output-431.eps"
set xlabel "Threads"
set ylabel "Time"
plot "benchmarks-20150919-431.txt" using 2:3 with errorlines