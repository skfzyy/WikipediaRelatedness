#!/bin/bash

# Bash script for benchmarking all relatedness mea
#



# ======================================================================================================================
#
# Constants.
#
# ======================================================================================================================

GRAPHS=( "out" "in" "sym" )


# ======================================================================================================================
#
# Utility functions.
#
# ======================================================================================================================


#
# Bash printing.
#
function logging_info {
    printf "[Bash] $1"
}


#
# Runs sbt with the specified arguments.
#
function run_sbt {
    args=$1
    ./sbt/bin/sbt "run $args"
}


# ======================================================================================================================
#
# Benchmarking functions.
#
# ======================================================================================================================


#
# Experiments Milne&Witten relatedness with different type of graphs.
#
function run_milnewitten {
    for graph in "${GRAPHS[@]}"
    do

        args="--name milnewitten --graph $graph"
        logging_info "Experimenting MilneWitten with paramters: $args\n"

        run_sbt "$args"

    done
}


#
# Experiments Jaccard relatedness with different type of graphs.
#
function run_jaccard {
    for graph in "${GRAPHS[@]}"
    do

        args="--name jaccard --graph $graph"
        logging_info "Experimenting Jaccard with paramters: $args\n"

        run_sbt "$args"

    done
}


#
# Experiments JaccardLocalClustering relatedness with different type of graphs.
#
function run_jaccardlocalclustering {
    for graph in "${GRAPHS[@]}"
    do

        args="--name jaccardlocalclustering --graph $graph"
        logging_info "Experimenting JaccardLocalClustering with paramters: $args\n"

        run_sbt "$args"

    done

}


#
# Experiments CosineLocalClustering relatedness with different types of graphs and thresholds.
#
function run_cosinelocalclustering {
    thresholds=( 0 10 50 100 500 1000 2000 5000 )

    for graph in "${GRAPHS[@]}"
    do
        for threshold in "${thresholds[@]}"
        do

            args="--name cosinelocalclustering --graph $graph --threshold $threshold"
            logging_info "Experimenting CosineLocalClustering with paramters: $args\n"

            run_sbt "$args"

        done
    done
}


#
# Experiments ESA relatedness with different thresholds.
#
function run_esa {
    thresholds=( 100 200 500 1000 1500 2000 2500 3000 5000 10000 )

    for threshold in "${thresholds[@]}"
    do

        args="--name esa --threshold $threshold"
        logging_info "Experimenting ESA with paramters: $args\n"

        run_sbt "$args"


        args="--name esaentity --threshold $threshold"
        logging_info "Experimenting ESAEntity with paramters: $args\n"

        run_sbt "$args"

    done
}


#
# Experiments SVD relatedness with different thresholds.
#
function run_svd {
    thresholds=( 10 50 100 150 200 )

    for threshold in "${thresholds[@]}"
    do

        args="--name svd --threshold $threshold"
        logging_info "Experimenting SVD with paramters: $args\n"

        run_sbt "$args"

    done
}


#
# Experiments LDA relatedness with different thresholds.
#
function run_lda {
    thresholds=( 10 50 80 100 )

    for threshold in "${thresholds[@]}"
    do

        args="--name lda --threshold $threshold"
        logging_info "Experimenting LDA with paramters: $args\n"

        run_sbt "$args"

    done
}


#
# Experiments w2v-based relatedness with different embedding models.
#
function run_w2v {
    models=(
             # text-based (standard w2v)
             "w2v.corpus" "w2v.coocc" "w2v.sg"

             # ask to Francesco
             "el.el_1st_dw" "el.el_1st" "el.el_dw"

             # random walk-based (DeepWalk)
             "deepwalk.dw" "deepwalk.deep_corpus"
             "deepwalk.dw10" "deepwalk.dw30" "deepwalk.dw50" "deepwalk.dw70" "deepwalk.dw90"\
             "deepwalk.dwsg"
    )

    for model in "${models[@]}"
    do

        args="--name w2v --model $model"
        logging_info "Experimenting Word2Vec Embedding with paramters: $args\n"

        run_sbt "$args"

    done
}



# language model?


#
# Experiments based on clique graph generation.
#
function run_clique {
    subNodes=( "esa" "esaentity" "sg" "dwsg" )
    subSizes=( 10 50 100 )

    weighters=( "milnewitten" "jaccard" "w2v" )
    weighterGraphs=("in" "sym")
    weighterModels=( "w2v.sg" "deepwalk.dwsg" )

    simRankers=( "ppr" "csr" )
    iterations=( 3 10 30 50 )
    pprAlphas=( 0.1 0.2 )
    csrDecays=( 0.7 0.8 0.9 )


    #subNodes=( "esa" )
    #subSizes=( 10 50 100 )

    #weighters=( "milnewitten" )
    #weighterGraphs=( "in" )
    #weighterModels=( "w2v.sg" )

    #simRankers=( "csr" )
    #iterations=( 30 )
    #pprDampings=( 0.1 )
    #csrDecays=( 0.9 )

    # WTF re-write it with recursion...
    for nodes in "${subNodes[@]}"
    do
        for size in "${subSizes[@]}"
        do
            for weight in "${weighters[@]}"
            do
                for simRank in "${simRankers[@]}"
                do
                    for iters in "${iterations[@]}"
                    do
                        for graph in "${weighterGraphs[@]}"
                        do
                            for model in "${weighterModels[@]}"
                            do
                                for ppr in "${pprAlphas[@]}"
                                do
                                    for csr in "${csrDecays[@]}"
                                    do

                                        args="--name clique --subNodes $nodes --subSize $size --weighter $weight "
                                        args+="--weighterGraph $graph --weighterModel $model --simRanker $simRank "
                                        args+="--iterations $iters --pprAlpha $ppr --csrDecay $csr"

                                        logging_info "Experimenting Clique Relatedness with parameters: $args\n"

                                        run_sbt "$args"

                                    done
                                done
                            done
                        done
                    done
                done
            done
        done
    done

}


# ======================================================================================================================
#
# Main
#
# ======================================================================================================================


run_clique

run_milnewitten
run_jaccard

run_jaccardlocalclustering
run_cosinelocalclustering

run_esa

run_svd
run_lda

run_w2v
