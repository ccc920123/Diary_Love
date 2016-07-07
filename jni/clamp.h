 double FClampDouble(  double t,  double tLow,  double tHigh)
		{
			if (t < tHigh)
			{
				return ((t > tLow) ? t : tLow) ;
			}
			return tHigh ;
		}

		 int FClamp0255( double d)
		{
			return (int)(FClampDouble(d, 0.0, 255.0) + 0.5) ;
		}

        int FClamp(int t, int tLow, int tHigh)
		{
			if (t < tHigh)
			{
				return ((t > tLow) ? t : tLow) ;
			}
			return tHigh ;
		}
        //´´½¨Ô²
        double AngleToRadian (int nAngle) {return 3.14159265358979323846 * nAngle / 180.0;}
