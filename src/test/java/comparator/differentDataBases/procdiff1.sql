create procedure example_procedure(in arg1 int, inout arg2 int)
begin
    set arg2 = arg1 + arg2;
end;
