#!/usr/bin/env bash

function run_sbt {
    args=$1
    ./sbt/bin/sbt "run $args"
}


mw=("uncom.mw" "com.mw")
dw=("uncom.dw" "com.dw")

mwdw=("${mw[@]}" "${dw[@]}")


# Single Method Evaluation
for method in "${mwdw[@]}":
    do
        run_sbt "--name $method"
    done


# Algorithmic Scheme Evaluation
for mw in "${mw[@]}":
do
    for dw in "${dw[@]}":
    do
        run_sbt "--name algo:$mw+$dw"
    done
done