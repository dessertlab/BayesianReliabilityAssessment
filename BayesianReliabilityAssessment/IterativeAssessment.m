function [marg_distr, meanPFD] = IterativeAssessment(cumulative_demands,cumulative_failures, dirichlet_step, beta_step, model_selection)

%  Compute the marginal distribution of the system PFD, the conditional
%  distributions and related statistics at the end of 'n' iterations with
%  observations on issued and failed demands. 
%  Input: 
%  - cumulative_demands: (n x m) matrix of cumulative demands (with n being the number of iterations and m the number of partitions)
%  - cumulative_failures: (n x m) matrix of cumulative failing demands (with n being the number of iterations and m the number of partitions)
%  - dirichlet_step: The step used for discretization in the Dirichilet PDF
%  computation
%  - beta_step: The step used for discretization in the Beta PDFs computation
%  - model_selection: if set to 1, the dynamic model selection appraoch (by Bayes factor) is appleid to
%  select the model; if different than 1, the dynamic model selection is disabled

%  Output: 
%   - marg_distr: The output PFD distribution (n-by-1 matrix) 
%   - mean of marg_distr

marg_distr = zeros(length(cumulative_demands), beta_step);
meanPFD = zeros(1,length(cumulative_demands));
for iteration=1:length(cumulative_demands)
    if model_selection==1
        fprintf('Iterative assessment with dynamic model selection: ')
        iteration=iteration
        
        if iteration>1
            observationsLastIter=cumulative_demands(iteration,:)- cumulative_demands(iteration-1,:)
            accuracy = 50;
            vol_unit = power(1/accuracy,length(cumulative_demands(1,:))-1)
            w_history = exp(dirmnpdfln(observationsLastIter,cumulative_demands(iteration,:)+1))*vol_unit
            wo_history = exp(dirmnpdfln(observationsLastIter,observationsLastIter+1))*vol_unit
            
            BayesFactor = w_history/wo_history
            if BayesFactor >1
                alphaDirichlet = cumulative_demands(iteration,:) +1;
                alpha = cumulative_failures(iteration,:) + 1 ; 
                beta = cumulative_demands(iteration,:)-cumulative_failures(iteration,:) + 1  ;
                fprintf('With-history selected ')
            else
                alphaDirichlet = observationsLastIter +1;
                alpha = cumulative_failures(iteration,:) - cumulative_failures(iteration-1,:) + 1 ; 
                beta = observationsLastIter-(cumulative_failures(iteration,:) - cumulative_failures(iteration-1,:)) + 1  ;
                fprintf('Without-history selected ')
            end
        else
            alphaDirichlet = cumulative_demands(iteration,:) +1;
            alpha = cumulative_failures(iteration,:) + 1 ; 
            beta = cumulative_demands(iteration,:)-cumulative_failures(iteration,:) + 1  ;
        end
    else
        fprintf('Iterative assessment without dynamic model selection: ')
        iteration=iteration
        alphaDirichlet = cumulative_demands(iteration,:) +1;
        alpha = cumulative_failures(iteration,:) + 1 ; 
        beta = cumulative_demands(iteration,:)-cumulative_failures(iteration,:) + 1  ;
    end
    [marg_distr(iteration,:)] = total_prob_failure_pdf(alphaDirichlet, dirichlet_step, beta_step, alpha, beta);
    prob=(linspace(1,beta_step,beta_step))./beta_step;
    meanPFD(iteration) = sum(prob.*marg_distr(iteration, :));
end

end

