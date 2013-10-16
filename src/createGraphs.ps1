param (
    [string[]]$logfileNames,
    [string[]]$legendNames,
    [string]$output = "graph.png",
    [string]$title = "Prubeh ucelove hodnoty",
    [string]$logScale = "y",
    [string]$path = $(pwd),
    [int]$scale = 1,
    [int]$barsEvery = 20
)   

$plot = "set term pngcairo solid lw 2
		set output `'$output`'
		set logscale $logScale
		set grid
		set title '$title'
		set xlabel 'Function evaluations (/$scale)'
		set ylabel 'Objective value'
		plot "    

foreach ($logFile in $logFileNames) {

	$color = $logFileNames.IndexOf($logFile);
	$name = $legendNames.Get($color)

	$plot = $plot + "`'$path\$logFile.objective_stats`' using (`$1/$scale):4 w l title `'$name`' ls 1 lc $color, `'$path\$logFile.objective_stats`' every $barsEvery using (`$1/$scale):4:3:5 with yerrorbars notitle ls 1 lc $color,"
}

$plot = $plot.Substring(0,$plot.Length - 1);

$plot = $plot + "
		set output
		set term wxt"

$plot | gnuplot.exe
