#!/usr/bin/env bash

sbt clean coverage test && sbt coverageReport
