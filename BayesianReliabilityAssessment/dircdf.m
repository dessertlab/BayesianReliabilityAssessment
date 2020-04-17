function [x, ind_space, PDF] = dircdf(alpha, dirchlet_step)


dims = length(alpha);
steps = dirchlet_step;
ind_space = struct('indeces',[],'pdf',[],'volume',[],'x',[]);
inds = get_next_index_new(steps,dims);
% now convert the string into a cell of strings
x = zeros(1,dims);
y = zeros(1,dims);
integral = 0;
vol_unit = power(1/steps,dims-1);
x = inds / steps;
for i=1:length(x)
    ind_space(i).indeces = inds(i,:);
    ind_space(i).x = x(i,:);
    D = exp(dirmnpdfln(x(i,:),alpha)); 
    ind_space(i).pdf = D;
    ind_space(i).volume = D * vol_unit;
    integral = integral + D * vol_unit;
end

integral2 = 0;
for i=1:length(x),
 ind_space(i).volume = ind_space(i).volume / integral;   % compensate rounding errors and set the integral to 1. 
 integral2 = integral2 + ind_space(i).volume;
 PDF(i) = ind_space(i).volume;
end
%display(integral)
%display(integral2)

end
