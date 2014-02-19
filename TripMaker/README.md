TripMaker
=========

Graph Package
==============

Because Java does not have a graph package, I created one myself and implemented breadth-first, depth first, and normal graph traversals.

Makefile
========

An input file (“makefile”)  consists of rules that indicate that one set of objects (called targets) depends on another set (called prerequisites)

T: P1 P2 ···Pm

For each T, 

- if the makefile does not have a rule with T as the target, and T does not currently exist, there is an error. If T does exist, it is considered “built.”

- Otherwise, Make considers all the headers in the makefile whose target is T and collects the set of all prerequisites listed in those headers. It first builds all of these prerequisites, applying the procedure being described here. Next, if T does not exist, or if T is older than at least one of its prerequisites, Make executes the non-empty commands set from one of these rules, and then sets the creation time of T to be larger than that of any existing object (so that T is now the youngest object). It is an error if there is more than one non-empty set of commands for a target. Make never rebuilds something unnecessarily.

Build in this case means outputing to the standard output or target.

The Makefile utilizes depth-first traversal that was implemented in the graph package.


To run:

java make.Main [ -f MAKEFILE ] [ -D FILEINFO ] [ TARGET ... ]

[...] is an optional argument

TripMaker
=========

TripMaker takes in data of locations (-m), the OUT (default standard output) , and a series of requests that contains at least 2 locations.

The data of locations consists of two forms:

Location entries with the form 

L C X Y

where C designates a place and X and Y are floating-point numbers. This means that the place named C is at coordinates (X, Y ) (we’re sort of assuming a flat earth here.) There may only be one location entry for any given C.

Distance entries with the form

R C0 N L D C1

where each Ci designates a place, N is the name of a road L is a numeric distance (a floating-point number), and D is one of the strings NS, EW, WE, SN. Each Ci must be declared in a previous location entry.
This implements A* breadth-first traversal.

To run:

java trip.Main [ -m MAP ] [ -o OUT ] [ REQUESTS ]
