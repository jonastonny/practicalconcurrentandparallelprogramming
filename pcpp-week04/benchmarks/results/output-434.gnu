# Output script for 4.3.4
set terminal postscript color solid
set output "output-434.eps"
set xlabel "Threads"
set ylabel "Time"
plot "benchmarks-20150919-434.txt" using 2:3 with errorlines
