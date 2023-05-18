# Base image
FROM silverlogic/python3.8

# Set the working directory in the container
WORKDIR /usr/src/app

# Set the PYTHONPATH environment variable
ENV PYTHONPATH /usr/src/app:$PYTHONPATH
ENV DAIKONDIR=/usr/src/app/daikon

# Install system dependencies
RUN apt-get update && \
    apt-get install -y \
        openjdk-11-jdk \
        curl \
        perl \
        make \
        rsync \
        netpbm \
        texinfo \
        texlive \
        dos2unix \
        nano \
        binutils-dev && \
    apt-get clean

# Install Node.js
RUN curl -sL https://deb.nodesource.com/setup_14.x | bash - && \
    apt-get install -y nodejs

# Copy the required files into the container
COPY server-app/server ./server
COPY invcon ./invcon
COPY daikon-5.8.6 ./daikon

# Install any dependencies
RUN pip install -e invcon && \
    pip install slither-analyzer PySocks lxml

# Install Node.js dependencies
RUN npm install web3-eth-abi --save && \
    npm install commander --save

# Convert files to Unix format (yes this is stupid, but it works)
RUN find / -type f -exec dos2unix {} \;


# Set up Daikon
RUN /bin/bash -c "source $DAIKONDIR/scripts/daikon.bashrc" && \
    make -C $DAIKONDIR rebuild-everything

# Expose port 8080
EXPOSE 8080

# Run the command
CMD [ "python", "-m", "server.main" ]
