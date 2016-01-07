-module(math1).
-export([fac/1]).
-export([double/1]).
-export([times/2]).

fac(0) -> 1;
fac(N) -> N * fac(N-1).

double(X) -> times(X, 2).
times(X, N) -> X * N.