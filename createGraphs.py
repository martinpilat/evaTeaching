import argparse
import csv
from matplotlib import pyplot as plt
import numpy as np

parser = argparse.ArgumentParser(description='Create graphs from evolution logs')
parser.add_argument('-f', '--files', metavar='F', type=str, nargs='+', help='Names of the input files', required=True)
parser.add_argument('-l', '--labels', metavar='L', type=str, nargs='+', help='Labels for graphs from files', required=True)
parser.add_argument('--logscale', metavar='S', choices=['x', 'y', 'both'], help='Set logarithmic axis (x, y, or both)')
parser.add_argument('-o', '--output', metavar='O', type=str, help='Name of the output file', default='output.png')
parser.add_argument('-s','--scale', metavar='N', type=int, help='Scaling for the numbers on x-axis', default=1)
parser.add_argument('--limit', metavar='N', type=int, help='The upper limit for the x-axis')
parser.add_argument('-t', '--title', metavar='T', type=str, help='The plot title', default='Objective values plot')

args = parser.parse_args()

xmax = None
for file, label in zip(args.files, args.labels):
    rows = []
    print(file)
    with open(file) as stats:
        stats_reader = csv.reader(stats, delimiter=' ')
        for row in stats_reader:
            rows.append(list(map(float, row)))
    arr = np.array(rows)
    xmax = np.max(arr[:, 0])
    plt.plot(arr[:, 0]/args.scale, arr[:, 3], label=label, alpha=0.5)
    plt.fill_between(arr[:, 0]/args.scale, arr[:, 2], arr[:,4], alpha=0.5)
    print(arr)

if args.limit:
    xmax = min([xmax, args.limit])

xmax = xmax/args.scale

if args.logscale:
    if args.logscale == 'x':
        plt.semilogx()
    if args.logscale == 'y':
        plt.semilogy()
    if args.logscale == 'both':
        plt.loglog()

plt.title(args.title)
plt.xlabel('Number of fitness evaluations' + (f' (in {args.scale}\'s)' if args.scale != 1 else ''))
plt.xlim(1, xmax)
plt.ylabel('Objective value')
plt.tight_layout()
plt.legend()
plt.savefig(args.output)