function [ beta_pdf ] = gen_beta_scaled(alfa, beta, scale, steps)
%gen_beta() 
%  returns a discretised beta_pfd scalled to fit in the range [0,scale
%  Parameters:
%   - scale is an interger in the range [0,1]
%   - alfa, beta - the parameters of the standard beta distribution
%   - steps - a parameter of discretisation used for the range [0,1].

upper_bound = scale * steps;
x_value = (1:upper_bound)./upper_bound;
cdf_value = cdf('Beta', x_value, alfa, beta);
beta_pdf = diff([0 cdf_value]); 

end