#!/usr/bin/env bash
sudo chmod -R 777 $HOME/.ivy2
sudo chmod -R 777 $HOME/.sbt
sudo chmod -R 777 $HOME/.cache
sudo rm -rf target
sudo rm -rf services/streaming/target
sudo rm -rf services/metrics/target
sudo rm -rf project/target
sudo rm -rf project/project/target