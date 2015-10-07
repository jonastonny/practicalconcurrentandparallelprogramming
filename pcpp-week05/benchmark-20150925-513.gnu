# Output script for 5.1.3
set terminal postscript color solid
set output "output-513.eps"
set xlabel "Tasks"
set ylabel "Time"
plot "benchmarks-20150925-511.txt" using 2:3 title "CachedThreadPool" with errorlines,"benchmarks-20150925-512.txt" using 2:3 title "WorkStealingPool" with errorlines
