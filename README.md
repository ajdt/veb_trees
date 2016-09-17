## background 
This is an old project from Spring 2010 that I wanted to keep. Van Emde Boas (VEB) trees have constant time min/max
lookups and suport O(log log n) update operations. There's a chapter on VEB Trees in the CLRS algorithms book 
where I first learned of them.

## the project
The project provides an implementation (VEB.java) of a VEB Tree, and an implementation of an AVL tree. 
My goal was to profile their execution times (using System.nanoTime()) on a large data-set to see if 
I could empirically observe the asymptotic advantage VEB trees have over a traditional balanced
binary tree. In the end, I couldn't find any appreciable run-time differences.
