local currentValue = redis.call('get', KEYS[1]);
local step = tonumber(ARGV[1]);
local maxId = tonumber(ARGV[2]);

if not currentValue then
    currentValue = 0;
elseif type(currentValue) == 'string' then
    currentValue=tonumber(currentValue);
end



if currentValue >= maxId then
    return "-1";
end

if (currentValue + step > maxId)
then
    step = maxId - currentValue;
end

if (step > 0) then
    return tostring(redis.call('INCRBY', KEYS[1], step)) ;
else
    return "-1";
end





