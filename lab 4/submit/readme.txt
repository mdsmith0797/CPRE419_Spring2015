The code here is specified for sorting the largest file (250M records):

run jobpar1.file and jobpart2.file sequentially the sorted file will be outputed in scr/shuowang/lab4/exp1/output






To sort other files (not the 250M one)

1. run jobpart1.file
2. use hdfs dfs -cat /scr/shuowang/lab4/exp1/temp/part-r-00000 to check the calculated partitioning points
3. customize the partitiong points in dummysort2.java and complie a new dummysort2.jar
4. run jobpart2.file 

the sorted file will be outputed in scr/shuowang/lab4/exp1/output



For details please check lab4_shuowang.pdf