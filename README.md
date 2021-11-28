# GAvI-Project (Good Search)

This is the project for the exam of Advanced Management of the Information (GAvI in italian). The scope is building a textual search engine through Lucene library (https://lucene.apache.org/). Several models have been exploited to handle different kinds of queries, specifically:
- Boolean
- Fuzzy
- Vectorial
- Probabilistic (BM25)
Each model defines the way to determine the similarity (then, the ranking) of each document during the query formulation.
The project provides a GUI to add documents to the index used for research, which can be saved and loaded as an independent file. The search will be performed only on the files listed in the index, through the "Inverted Indexing" used by Lucene.  

## Requirements

Lucene library

## How to use it

JAR file (GoodSearch.jar) is executable giving the command:

	java -jar GoodSearch.jar 
in the command line. Otherwise, just launch

	java src/gui/Main_Window.java
Reference to "HELP" button in GUI to get advises on query formulation.

## Documentation
Documentation is available in "doc" folder, readable opening index.html file using an internet browser

## Benchmark 
Using benchmark button in GUI will start a benchmark test on GoodSearch, using selected IR Model. This will produce some plot files, contained under 

    benchmark/lisa/Results

## Media
All media files (logo, screenshots, ...) of the project have been stored inside this folder.

## Src
Folder containing the source code (refer to documentation to know how to compile it on your own)

## Other
This folder is intended to contain all the files which don't fit other categories (i.e. text files for debug)
