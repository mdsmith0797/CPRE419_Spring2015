
--Load data
input_lines = LOAD '/class/s15419x/lab7/historicaldata.csv' USING PigStorage(',') 
AS (ticker:chararray,date:int,open:double,high:double,low:double,close:double,volume:long);

-- Keep only the opening price field
project_open = FOREACH input_lines GENERATE ticker,date,open;

-- Filter the stock_prices to two time periods: 19900101-20000103 and 20050102-20140131
stock_price1 = FILTER project_open BY date>=19900101 and date<=20000103;
stock_price2 = FILTER project_open BY date>=20050102 and date<=20140131;

Hot = FILTER stock_price1 BY (ticker=='HOT');

STORE Hot INTO '/scr/shuowang/lab7/exp1/output3/';



