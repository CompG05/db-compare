create function example_function(arg1 int, arg2 int) returns int deterministic
begin
    return arg1 + arg2;
end;