function [ output ] = get_next_index_new(steps, dims)

s = steps-dims;
n = dims;
v = 1:(s+n-1);

Indexes = nchoosek(v,n-1);
output=zeros(length(Indexes), n);
for i=1:length(Indexes)
    output(i,1) = Indexes(i,1)-1;
    for j=2:length(Indexes(1,:))
        output(i,j) = (Indexes(i,j)-Indexes(i,j-1))-1;
    end
    output(i,n) = length(v) - Indexes(i,length(Indexes(1,:)));
end
output = output+1;
end

