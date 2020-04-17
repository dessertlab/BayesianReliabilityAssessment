function [offset,sPFD] = KalmanReliabilityAssessment(demands, failures,truePFD)

%  Compute the estimate of the expected value of the probability of failure
%  on demand (PFD) by a non stationary dicsrete Kalman filter.
%  Input: 
%  - demands: (n x m) matrix of (non-cumulative) demands (with n being the number of iterations and m the number of partitions)
%  - failures: (n x m) matrix of (non-cumulative) failing demands (with n being the number of iterations and m the number of partitions)
%  - truePFD (optional): for our experimetnation purpose, the value of the true PFD
%  needed to compute the offset and the plot. 
%  Output: 
%   - offset: The offset (difference between the estimated and true PFD). If the truePFD is not provided, offset=[]; 
%   - sPFD: structure containing the output of the Kalman filter at each
%   iteration (see the auxiliary file klamanf.m for details about the
%   structure)

    %Filter Initialization
    s = size(demands); 
    number_of_iterations=s(1);
    partitions=s(2);
    alphaDirichlet = ones(1,partitions);
    alpha = ones(1,partitions);
    beta = ones(1,partitions);
    p = (alphaDirichlet)./sum(alphaDirichlet);
    f = alpha./(alpha+beta);
    pOld = p;
    fOld = f;
    PFDold = pOld*fOld';
    deltaP  = 0; 
    deltaF = 0;
    
    %sPDF is the structre used for exchanging I/O with Kalman filter
    %implementation in each iteration
    
    sPFD.A=  1+ p*deltaF'/PFDold + f*deltaP'/PFDold  + deltaP*deltaF'/PFDold ; 
    
    %Variance under independence
    
    varianceBeta = (alpha.*beta)./( (alpha+beta).^2.*(alpha+beta+1) );
    varianceP = (alphaDirichlet .* (sum(alphaDirichlet) - alphaDirichlet))/(sum(alphaDirichlet)^2*(1+sum(alphaDirichlet)));
    variancePFD = sum(p.^2.*varianceBeta  + f.^2.*varianceP);
    
    sPFD.Q = variancePFD;
    sPFD.R = variancePFD; 
    
    %Further initialization
    sPFD.x = nan;
    sPFD.P = nan;
    sPFD.H=1; 
    sPFD.u = 0;
    sPFD.B=0;
  
    
    %start iteartion
    offset=[];
    for iteration=1:number_of_iterations
        iteration

        PFD = sum(failures(iteration,:)./sum(demands(iteration,:))); %From Observations
        sPFD(end).z= PFD;
        sPFD(end+1)=kalmanf(sPFD(end)); % perform a Kalman filter iteration
        if nargin == 3
            offset(iteration)=sPFD(end).x - truePFD;  
        end

        %Prepare for the next iteration
  
        %Update with new observations
        alphaDirichlet = demands(iteration,:) +1;
        alpha = failures(iteration,:) +1 ; 
        beta = demands(iteration,:)-failures(iteration,:) +1;  
        p = (alphaDirichlet)./sum(alphaDirichlet);
        f = alpha./(alpha+beta);
        deltaP  = 1/2.*(p - pOld);
        deltaF = 1/2.*(f - fOld);
        sPFD(end).A=  1+ p*deltaF'/PFDold + f*deltaP'/PFDold  + deltaP*deltaF'/PFDold;
        pOld = p;
        fOld = f;
        PFDold = pOld*fOld';
        
        varianceBeta = (alpha.*beta) ./ ( (alpha+beta).^2.*(alpha+beta+1) );
        varianceP = (alphaDirichlet .* (sum(alphaDirichlet) - alphaDirichlet))/(sum(alphaDirichlet)^2*(1+sum(alphaDirichlet)));
        variancePFD = sum(p.^2.*varianceBeta  + f.^2.*varianceP);
        sPFD(end).Q = variancePFD;   
        sampleVariance = var([ones(1,sum(failures(iteration,:))) zeros(1,sum(demands(iteration,:)) - sum(failures(iteration,:)))]); 
        sPFD(end).R = sampleVariance;
        
        sPFD(end).H=1; 
        sPFD(end).u = 0;
        sPFD(end).B=0;
    end
    if number_of_iterations>1
        truePFD = ones(number_of_iterations,1).*truePFD;
        n=1:number_of_iterations;
        plot(n,[sPFD(1:end-1).z]','r.', n, [sPFD(2:end).x]','b-', n,truePFD,'g-');
    end
end

