create function example_procedure(in arg1 int, in arg2 int) returns int
begin
    set arg2 = arg1 + arg2;
    return arg2;
end;