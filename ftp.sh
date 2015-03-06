#!/bin/bash
HOST=$1
USER=$2
sftp $USER@$HOST < ftp-batch.txt 
