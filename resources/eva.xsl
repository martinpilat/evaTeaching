<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<head>
				<title>Log date <xsl:value-of select="/log/@date" /></title>
				<style>
					table
					{
						border-collapse:collapse;
					}
					table,th, td
					{
						border: 1px solid black;
					}
					td, th
					{
						padding: 4px;
					}
				</style>
			</head>
			<body>
				<h1>Log date <xsl:value-of select="/log/@date" /></h1>
				<h2>Properties</h2>
				<table border="1">
					<tr><th>Name</th><th>Value</th></tr>
					<xsl:for-each select="/log/properties/property">
						<xsl:sort select="@name"/>
						<tr>
						<td><xsl:value-of select="@name"/></td>
						<td><xsl:value-of select="."/></td>
						</tr>
     				</xsl:for-each>
				</table>
				<h2>Initial population</h2>
				<table border="1">
					<tr><th>ID</th><th>Chromosome</th><th>Fitness</th><th>Objective</th></tr>
					<xsl:for-each select="/log/initial-population/individual">
						<xsl:sort select="fitness" order="descending" data-type="number"/>
						<tr>
						<td><xsl:value-of select="position()"/></td>
						<td><xsl:value-of select="chromosome"/></td>
						<td><xsl:value-of select="fitness"/></td>
						<td><xsl:value-of select="objective"/></td>
						</tr>
     				</xsl:for-each>
				</table>
				<xsl:for-each select="/log/generation">
					<h2>Generation <xsl:value-of select="@number"/></h2>
					<h3>Mating selection</h3>
					<table border="1">
						<tr><th>ID</th><th>Chromosome</th><th>Fitness</th><th>Objective</th></tr>
						<xsl:for-each select="mating-selection/individual">
							<xsl:sort select="fitness" order="descending" data-type="number"/>
							<tr><td><xsl:value-of select="position()"/></td>
							<td><xsl:if test="@selected-by = ''">
								<div style="text-decoration:line-through;">
									<xsl:value-of select="chromosome"/>
								</div>
							</xsl:if>
								<xsl:if test="@selected-by != ''">
									<div style="font-weight:bold;" title="{normalize-space(@selected-by)}">
										<xsl:value-of select="chromosome"/>
									</div>
								</xsl:if>
							</td>
								<td><xsl:value-of select="fitness"/></td>
								<td><xsl:value-of select="objective"/></td>
                            </tr>
						</xsl:for-each>
					</table>
					<h3>Operators</h3>
					<table border="1">
					<tr>
						<th>ID</th><th>Mating pool</th>
						<xsl:for-each select="operator"><th><xsl:value-of select="@name"/></th></xsl:for-each>
					</tr>
					<xsl:for-each select="mating-pool/individual">
						<xsl:variable name="index" select="position()"/>
	     				<tr>
	     					<td><xsl:value-of select="position()"/></td>
	     					<td><xsl:value-of select="chromosome"/></td>
	     					<xsl:for-each select="../../operator">
	     						<td><xsl:value-of select="individual[$index]/chromosome"/></td>
	     					</xsl:for-each>
	     				</tr>
     				</xsl:for-each>
     				</table>
     				<h3>Environmental Selection</h3>
     				<table border="1">
     					<tr><th>ID</th><th>Chromosome</th><th>Fitness</th><th>Objective</th></tr>
     					<xsl:for-each select="environmental-selection/individual">
     						<xsl:sort select="fitness" order="descending" data-type="number"/>
     					<tr><td><xsl:value-of select="position()"/></td>
     						<td><xsl:if test="@selected-by = ''">
     						<div style="text-decoration:line-through;">
     							<xsl:value-of select="chromosome"/>
     						</div>
     						</xsl:if>
     						<xsl:if test="@selected-by != ''">
     						<div style="font-weight:bold;" title="{normalize-space(@selected-by)}">
     							<xsl:value-of select="chromosome"/>
     						</div>
     						</xsl:if>
     					</td>
     					<td><xsl:value-of select="fitness"/></td>
     					<td><xsl:value-of select="objective"/></td>
     					</tr>		
     					</xsl:for-each>
     				</table>
				</xsl:for-each>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>