call driver-service-build.bat
cd ../
call passenger-service-build.bat
cd ../
call payment-service-build.bat
cd ../
call rating-service-build.bat
cd ../
call ride-service-build.bat
cd ../
call docker-compose up -d