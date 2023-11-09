create procedure example_procedure(in arg1 int, inout arg2 int, in arg3 varchar(10), out arg4 date)
begin
    set arg2 = arg1 + arg2;
end;
