function [marg_distr, cond_distr_params, meanPFD] = ReliabilityAssessment(demands, failures, dirichlet_step, beta_step)

%  Compute the marginal distribution of the system PFD, the conditional
%  distributions and related statistics. 
%  Input: 
%  - demands: (1 x m) matrix of demands (with m being the number of partitions)
%  - failures: (1 x m) matrix of failing demands (with m being the number of partitions)
%  - dirichlet_step: The step used for discretization in the Dirichilet PDF
%  computation
%  - beta_step: The step used for discretization in the Beta PDFs computation

%  Output: 

%   - marg_distr: The output PFD distribution (n-by-1 matrix)
%   - cond_distr_params: The conditional PFD distribution, one per partition 
%   - mean of marg_distr

alphaDirichlet = demands +1;
alpha = failures + 1; 
beta = demands -failures +1;  

[x, marg_distr, cond_distr_params] = total_prob_failure_pdf(alphaDirichlet, dirichlet_step, beta_step, alpha, beta);
prob=(linspace(1,5000,5000))./5000;
meanPFD = sum(prob.*marg_distr);

end

