#!/bin/bash
export DAIKONDIR=/usr/src/app/daikon
source $DAIKONDIR/scripts/daikon.bashrc
make -C $DAIKONDIR rebuild-everything
