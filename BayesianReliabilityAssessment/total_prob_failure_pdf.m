
function [marginal_distr, cond_distr_params] = total_prob_failure_pdf(dirichlet_alpha, dirchlet_step, beta_step, beta_alpha, beta_beta)

%  Compute the marginal distribution of the system PFD and the conditional
%  distributions
%  Input: 
%  - dirichlet_alpha: The parameters of the Dirichlet PDF (representing the operational
%  profile) 
%  - dirichlet_step: The step used for discretization in the Dirichilet PDF
%  computation
%  - beta_step: The step used for discretization in the Beta PDFs
%  computation
%    - beta_alpha, beta_beta: The parameters of the Beta PDFs (representing the failrue probabilities per partition)

%  Output: 
%   - marginal_distr: The output PFD distribution (n-by-1 matrix)
%   - cond_distr_params: The conditional PFD distribution, one per partition 


dims = length(dirichlet_alpha);

[x,ind_space, PDF] = dircdf(dirichlet_alpha, dirchlet_step);

size(x);
cond_distr_params = struct('partition',[],'alfa',[],'beta',[],'steps',[],'pdf',[]);
for i=1:dims
cond_distr_params(i).partition = i;
cond_distr_params(i).alfa = beta_alpha(i);
cond_distr_params(i).beta = beta_beta(i);
cond_distr_params(i).steps = beta_step;
end

for i=1:dims
    % get the distrbutions of the conditional probabilities of failure
    cond_distr_params(i).pdf = gen_beta_m(cond_distr_params(i).alfa,cond_distr_params(i).beta,cond_distr_params(i).steps);
end
marginal_distr = zeros(1,cond_distr_params(1).steps);
for i=1:length(x)
    for j=1:dims
        cond_distr = gen_beta_scaled(cond_distr_params(j).alfa,cond_distr_params(j).beta,ind_space(i).x(j),cond_distr_params(j).steps);
        if(j==1)
            marg_conv = cond_distr;
        else
            marg_conv = conv(marg_conv,cond_distr);
            marg_conv(length(marg_conv)+1) = double(0);
        end
    end
    % now apply the weigth to this convolution
    marg_conv = marg_conv * PDF(i); % ind_space(i).volume; 
    % add add it to the marginal distribution
    marginal_distr = marginal_distr + marg_conv;    
end
display(sum(marginal_distr));
end
