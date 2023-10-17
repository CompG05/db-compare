create procedure example_procedure(in arg1 int, inout arg2 int, out arg3 int)
begin
    set arg2 = arg1 + arg2;
    set arg3 = arg1 * arg2;
end;