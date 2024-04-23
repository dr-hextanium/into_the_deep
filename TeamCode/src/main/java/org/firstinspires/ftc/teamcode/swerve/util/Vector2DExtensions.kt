package org.firstinspires.ftc.teamcode.swerve.util

import dev.frozenmilk.util.units.position.Vector2D

fun Vector2D.perp() = Vector2D(-this.y, this.x)