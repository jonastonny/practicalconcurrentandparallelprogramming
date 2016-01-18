# Output script for 4.3.2
set terminal postscript color solid
set output "output-432.eps"
set xlabel "Threads"
set ylabel "Time"
plot "benchmarks-20150919-431.txt" using 2:3 with errorlines