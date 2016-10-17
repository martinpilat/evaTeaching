#!/bin/bash


#bash createGraphs2.sh -logFileNames sga/sgaLog -legendNames sga

output="graph.svg"
title="Objective value log"
logScale=""
path=$(pwd)
scale=1
barsEvery=20
while [ "$1" != "" ]; do
	case "$1" in
	-logFileNames)
        shift
	    logFileNames=$1
	    ;;
	-legendNames)
	    shift
	    legendNames=$1
	    ;;
	-output)
	    shift
	    output=$1
	    ;;
	-title)
	    shift
	    title=$1
	    ;;
	-logScale)
	    shift
	    logScale=$1
	    ;;
	-path)
	    shift
	    path=$1
	    ;;
	-scale)
	    shift
	    scale=$1
	    ;;
	-barsEvery)
	    shift
	    barsEvery=$1
	    ;;
	-limit)
	    shift
	    limit=$1
	    ;;
    esac
    shift
done

if [ -z $logScale ]; then
    plot="unset logscale\n"
else
    plot="set logscale $logScale\n"
fi


plot=${plot}"set term svg solid lw 1\n\
set output \"$output\"\n\
set grid\n\
set title \"$title\"\n\
set xlabel \"Function evaluations (/$scale)\"\n\
set ylabel \"Objective value\"\n\
plot [:$limit]"

logFileNames=`echo $logFileNames | tr ',' ' '`

color=0
for logName in $logFileNames ; do
    ((color++))
    name=`echo $legendNames | cut -d',' -f $color`

    plot=${plot}"\"${path}/${logName}.objective_stats\" using (\$1/${scale}):4 w l title \"$name\" ls 1 lc $color, \"${path}/${logName}.objective_stats\" every $barsEvery using (\$1/${scale}):4:3:5 with errorbars notitle ls 1 lc $color,"
done

plot=`echo $plot | sed 's/.$//'`

plot="$plot \n\
set output\n\
set term pop\n"

echo -e $plot | gnuplot
