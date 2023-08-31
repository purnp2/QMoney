# To get latest quotations:
#echo aapl
#curl https://api.tiingo.com/tiingo/daily/aapl/prices?token=8783737f8effeb1e3f239d886376dedd0a8d1630

#echo googl
#curl https://api.tiingo.com/tiingo/daily/googl/prices?token=8783737f8effeb1e3f239d886376dedd0a8d1630

# startDate and endDate
echo "aapl: <startDate, endDate>"
# With single-quote : WORKS
#curl 'https://api.tiingo.com/tiingo/daily/aapl/prices?startDate=2019-01-01&endDate=2019-02-03&token=8783737f8effeb1e3f239d886376dedd0a8d1630'

# With double-quotes : WORKS
curl "https://api.tiingo.com/tiingo/daily/aapl/prices?startDate=2019-01-01&endDate=2019-01-07&token=8783737f8effeb1e3f239d886376dedd0a8d1630"

# aapl == AAPL
curl "https://api.tiingo.com/tiingo/daily/AAPL/prices?startDate=2019-01-01&endDate=2019-01-07&token=8783737f8effeb1e3f239d886376dedd0a8d1630"

#curl https://api.tiingo.com/tiingo/daily/aapl/prices?token=8783737f8effeb1e3f239d886376dedd0a8d1630&startDate=2019-01-01&endDate=2019-02-03

# Calling the startDate ONLY is giving a lot of data, starting from that date till 2023.
#curl "https://api.tiingo.com/tiingo/daily/aapl/prices?startDate=2019-01-01&token=8783737f8effeb1e3f239d886376dedd0a8d1630"

# Calling endDate ONLY is also giving a lot-lot of data, starting from 2016 and back
#curl "https://api.tiingo.com/tiingo/daily/aapl/prices?endDate=2019-02-03&token=8783737f8effeb1e3f239d886376dedd0a8d1630"


