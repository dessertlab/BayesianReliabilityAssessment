Supplemental material for the article:

Reliability Assessment of Service-based Software under Operational Profile Uncertainty, Roberto Pietrantuono, Peter Popov, Stefano Russo, submitted for publication to Reliability Engineering and System Safety journal. 

The repository contains the following artefacts: 

- The Matlab code under the BayesianReliabilityAssessment folder. 
The file ReliabilityAssessment.m for the assessment of reliability in one iteration. It takes as input: 
* demands: an (1 x m) matrix of demands (with m being the number of partitions); 
* failures: an (1 x m) matrix of failing demands (with m being the number of partitions);
* dirichlet_step: the step used for discretization in the Dirichilet PDF  computation;
* beta_step: the step used for discretization in the Beta PDFs computation. 
It provides:
* marg_distr: The output PFD distribution (n-by-1 matrix);
* cond_distr_params: The conditional PFD distribution, one per partition;
* mean of marg_distr. 

The file IterativeAssessment.m is used for a a continuous assessment over k iterations, either by the model selection approach or without the model selection approach. It takes as input: 
* cumulative_demands: an (n x m) matrix of cumulative demands (with n being the number of iterations and m the number of partitions); 
* cumulative_failures: an (n x m) matrix of cumulative failing demands (with n being the number of iterations and m the number of partitions);
* dirichlet_step: the step used for discretization in the Dirichilet PDF computation; 
* beta_step: the step used for discretization in the Beta PDFs computation; 
* model_selection: if set to 1, the dynamic model selection appraoch (by Bayes factor) is appleid to select the model; if different than 1, the dynamic model selection is disabled. 
It provides: 
* marg_distr: The output PFD distribution (n-by-1 matrix);
* mean of marg_distr. 
The other files in the folder which are support functions used by these two files. 

- The Matlab code under the KalmanFilterAssessment folder. 
The file KalmanReliabilityAssessment.m for the iterative assessment of the expected PFD by means of a discrete time-varying Kalman filter formulation. takes as input:   
* demands: an (n x m) matrix of (non-cumulative) demands (with n being the number of iterations and m the number of partitions);
% failures: an (n x m) matrix of (non-cumulative) failing demands (with n being the number of iterations and m the number of partitions);
* truePFD (optional): for our experimentation purpose, the value of the true PFD to compute so as the offset and the plot. 
It provides: 
* offset: The offset (difference between the estimated and true PFD). If the truePFD is not provided, offset=[]; 
* sPFD: structure containing the output of the Kalman filter at each iteration (see the auxiliary file klamanf.m for details about the structure)
The folder contains kalmanf.m, a support function used by KalmanReliabilityAssessment.m. 

In both cases, reliability is simply R = 1 - PFD (namely, "1 - marg_distr" to obtain the distribution, or "1 - mean(marg_distr)" to obtain the expected reliability)

The file InputForAssessment.xlsx contains the demands and the failing demands obtained after testing the subject under test described in the paper, which is NLP-Building-blocks. These are the inputs to be provided to the Matlab files to reproduce the results in the paper. 

The JavaCode folder contains experimental code to ease the execution of tests on a generic subject (a REST web service). 
To use it:
- Import the source code in an IDE (e.g., Eclipse).
- Select a subject. 
- Define partitions (called Test Frames) and an operational profile for the subject under test, as exemplified in the Java code (file main.Java)
- Modify the endpoints of the endpoint of the service API to invoke (in the example there is the localhost of subject under test, the NLP-Prose APIs)
- Define input classes. The TestFrame class defines some example classes to generate Strings with different features. This can be customised depending on the need.
- Deploy the subject
- Run the main.class file. Results are printed on the console. The response code is used as oracle to get the number of demands and of failed demands. 

Finally, the source code of NLP-Building-Blocks is uploaded, with instructions on how to deploy it (via docker) and how to invoke it. The name of the APIs are in the source folder. 
