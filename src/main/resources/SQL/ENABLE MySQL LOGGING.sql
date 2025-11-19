# to enable Logging, execute these 4 lines
#
SET global general_log = 0;
TRUNCATE TABLE mysql.general_log;
SET global general_log = 1;
SET global log_output = 'table';

# once logging is enabled, you can view LOG entries with a query
#
select event_time, convert(argument using utf8) from mysql.general_log;
select *, convert(argument using utf8) from mysql.general_log;

# IMPORTANT: When you are done - turn OFF the Logging feature !
#
SET global general_log = 0;
TRUNCATE table mysql.general_log;