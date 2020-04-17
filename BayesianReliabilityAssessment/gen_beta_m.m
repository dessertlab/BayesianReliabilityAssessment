function [ beta_pdf ] = gen_beta_m(alfa, beta, steps)

x_value = (1:steps)./steps; 
cdf_value = cdf('Beta', x_value, alfa, beta);
beta_pdf = diff([0 cdf_value]); 
%sum = take_sum(beta_pdf);
%display(sum);
