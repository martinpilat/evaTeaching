param (
    [string[]]$logfileNames,
    [string[]]$legendNames,
    [string]$output = "graph.svg",
    [string]$title = "Objective value log",
    [string]$logScale = "y",
    [string]$path = $(pwd),
    [int]$scale = 1,
    [int]$barsEvery = 20,
    [string]$limit
)

if ($logScale.Equals("")) {
    $plot = "unset logscale
    "
}
else {
    $plot = "set logscale $logScale
    "
}
    

$plot = $plot + "set term svg solid lw 2
		set output `'$output`'
		set grid
		set title '$title'
		set xlabel 'Function evaluations (/$scale)'
		set ylabel 'Objective value'
		plot [:$limit]"    

foreach ($logFile in $logFileNames) {

	$color = $logFileNames.IndexOf($logFile);
	$name = $legendNames.Get($color)
    $color += 1

	$plot = $plot + "`'$path\$logFile.objective_stats`' using (`$1/$scale):4 w l title `'$name`' ls 1 lc $color, `'$path\$logFile.objective_stats`' every $barsEvery using (`$1/$scale):4:3:5 with yerrorbars notitle ls 1 lc $color,"
}

$plot = $plot.Substring(0,$plot.Length - 1);

$plot = $plot + "
		set output
		set term wxt"

$plot | gnuplot.exe
