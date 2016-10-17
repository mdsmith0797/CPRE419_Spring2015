
--Load data
input_lines = LOAD '/class/s15419x/lab7/historicaldata.csv' USING PigStorage(',') 
AS (ticker:chararray,date:int,open:double,high:double,low:double,close:double,volume:long);

-- Keep only the opening price field
project_open = FOREACH input_lines GENERATE ticker,date,open;

-- Filter the stock_prices to two time periods: 19900101-20000103 and 20050102-20140131
stock_price1 = FILTER project_open BY date>=20130801 and date<=20131031;


IBM = FILTER stock_price1 BY (ticker=='IBM');

STORE IBM INTO '/scr/shuowang/lab7/exp2/output3/';



