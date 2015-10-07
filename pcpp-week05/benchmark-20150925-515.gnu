# Output script for 5.1.5
set terminal postscript color solid
set output "output-515.eps"
set xlabel "Tasks"
set ylabel "Time"
plot "benchmarks-20150925-511.txt" using 2:3 title "CachedThreadPool" with errorlines,\
"benchmarks-20150925-512.txt" using 2:3 title "WorkStealingPool" with errorlines,\
"benchmarks-20150925-515-CachedThreadPool.txt" using 2:3 title "CachedThreadPool w. LongAdder" with errorlines,\
"benchmarks-20150925-515-WorkStealingPool.txt" using 2:3 title "WorkStealingPool w. LongAdder" with errorlines
